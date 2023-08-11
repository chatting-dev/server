package com.chatting.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatting.domain.chatjoin.ChatJoin;
import com.chatting.domain.chatjoin.ChatJoinRepository;
import com.chatting.domain.chatroom.ChatRoom;
import com.chatting.domain.chatroom.ChatRoomRepository;
import com.chatting.domain.topic.Topic;
import com.chatting.domain.topic.TopicRepository;
import com.chatting.domain.useraccount.UserAccountRepository;
import com.chatting.exception.BusinessException;
import com.chatting.exception.ErrorCode;
import com.chatting.kafka.configuration.KafkaConstants;
import com.chatting.presentation.dto.Message;
import com.chatting.presentation.dto.request.ChatRoomEnterRequest;
import com.chatting.presentation.dto.request.ChatRoomMakeRequest;
import com.chatting.presentation.dto.request.ChatRoomSendRequest;
import com.chatting.presentation.dto.request.ChatTopicMakeRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

	/*
	* 채팅방 입장할 때
	* -> chat_room 안에 있는 count +1 후 저장 맟 카프카 메세지로 현재 채팅방 사람 count + 1 메세지 보
	* 채팅방 퇴장할 때
	* -> chat_room 안에 있는 count +1 후 저장 맟 카프카 메세지로 현재 채팅방 사람 count + 1 메세지 보
	* 주제 만들기
	* -> 주제가 같은 이름이 있는지 check 후 topic 저장
	* 채팅방 만들기
	* -> 채팅방 이름이 있는 지 check 후 채팅방 저장
	* */

	/*
	주제 만들기
	-> 주제가 같은 이름이 있는지 check 후 topic 저장
	 */

	private final TopicRepository topicRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatJoinRepository chatJoinRepository;
	private final UserAccountRepository userAccountRepository;
	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;

	@PostMapping("/maketopic")
	public ResponseEntity<Void> makeTopic(@RequestBody @Validated final ChatTopicMakeRequest chatTopicRequest){
		if(topicRepository.existsByName(chatTopicRequest.name())){
			throw new BusinessException("동일한 주제가 존재합니다.", ErrorCode.CR_SAME_TOPIC);
		}
		Topic topic = chatTopicRequest.toEntity(chatTopicRequest.name());
		topicRepository.save(topic);
		return ResponseEntity.ok().build();
	}

	/*
	* 채팅방 만들기
	 * -> 채팅방 이름이 있는 지 check 후 채팅방 저장
	*/
	@PostMapping("/makeroom")
	public ResponseEntity<Void> makeRoom(@RequestBody @Validated final ChatRoomMakeRequest chatRoomRequest){
		if(!topicRepository.existsByName(chatRoomRequest.name())){
			throw new BusinessException("토픽이 존재하지 않습니다",ErrorCode.CR_NONE_TOPIC);
		}
		ChatRoom chatRoom = chatRoomRequest.toEntity(chatRoomRequest.name(),
			topicRepository.findByName(chatRoomRequest.name()));
		chatRoomRepository.save(chatRoom);
		return ResponseEntity.ok().build();
	}
	/*
	채팅방 입장할 때
	* -> chat_room 안에 있는 count +1 후 저장 맟 카프카 메세지로 현재 채팅방 사람 count + 1 메세지 보
	*/
	@PostMapping("/enterroom")
	public ResponseEntity<Void> enterRoom(@RequestBody @Validated final ChatRoomEnterRequest chatRoomEnterRequest){
		if(!chatRoomRepository.existsByName(chatRoomEnterRequest.name())){
			throw new BusinessException("채팅방이 존재하지 않습니다.", ErrorCode.CR_NONE_CHATROOM);
		}
		ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomEnterRequest.name());
		chatRoomRepository.updateChatRoomCount(chatRoom.getCount()+1);
		chatJoinRepository.save(chatRoomEnterRequest.toEntity(chatRoom, userAccountRepository.findByLoginId(
			chatRoomEnterRequest.loginId()).get()));
		kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, new Message(chatRoom.getCount().toString()));

		return ResponseEntity.ok().build();
	}

	@PostMapping("/exitroom")
	public ResponseEntity<Void> exitRoom(@RequestBody @Validated final ChatRoomEnterRequest chatRoomEnterRequest){
		if(!chatRoomRepository.existsByName(chatRoomEnterRequest.name())){
			throw new BusinessException("채팅방이 존재하지 않습니다.", ErrorCode.CR_NONE_CHATROOM);
		}
		ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomEnterRequest.name());
		chatRoomRepository.updateChatRoomCount(chatRoom.getCount()-1);
		kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, new Message(chatRoom.getCount().toString()));

		return ResponseEntity.ok().build();
	}

	@PostMapping("/kafka/sendmessage")
	public ResponseEntity<Void> sendMessageChatRoom(@RequestBody @Validated ChatRoomSendRequest chatRoomSendRequest) {
		kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC,new Message(chatRoomSendRequest.loginId(),
			chatRoomSendRequest.topic(), chatRoomSendRequest.chatRoom()));
		return ResponseEntity.ok().build();
	}

}
