package com.buysell.controller;

import com.buysell.domain.DTO.BoardDTO;
import com.buysell.domain.DTO.ChatMessageDTO;
import com.buysell.domain.entity.ChatMessage;
import com.buysell.domain.entity.ChatRoom;
import com.buysell.service.BoardService;
import com.buysell.service.ChatService;
import com.buysell.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("chat")
@MessageMapping("/chat")
public class ChatController {

    private ChatService chatService;
    private UserService userService;
    private BoardService boardService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms() {
        return "/chat/room";
    }

    //     모든 채팅방 목록
    @GetMapping(value = "/rooms", produces = "application/json")
    public ResponseEntity<List<ChatRoom>> room() {
        List<ChatRoom> chatRooms = chatService.findAllRoom();
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public List<ChatRoom> roomGET(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }

    // 채팅방 생성
    @RequestMapping( value = "/room" , method=RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<String> roomPOST(@RequestParam(value = "nickname") String nickname, Principal principal){
        //닉네임 존재여부 확인
        boolean result = userService.checkNickName(nickname);
        if(result == false){
            return new ResponseEntity<>("noNickname", HttpStatus.OK);
        }
        String status = chatService.createChatRoom(nickname, principal);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    //채팅방 삭제
    @GetMapping("/room/stop/{roomId}")
    public String deleteRoomGET(Principal principal,@PathVariable String roomId){
        System.out.println("DELETE ROOM");
        chatService.deleteChatRoom(roomId, principal.getName());
        return "redirect:/chat/room";
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetailGET(Model model, @PathVariable String roomId, HttpServletResponse response)throws IOException
    {
        if(chatService.checkAccess(roomId)) {
            model.addAttribute("roomId", roomId);
            System.out.println("true");
        }
        else {
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('부적절한 시도를 하였습니다.');location.href='/chat/room';</script>");
            out.flush();

        }
        return "/chat/room";
    }

    //메시지보내기
    @MessageMapping("/message")
    @SendToUser
    public void messageSEND(ChatMessageDTO chatMessageDTO, Principal principal) {
       chatService.createMessage(chatMessageDTO ,principal);

    }

    //메시지 조회-입장시
    @GetMapping("/messages/{roomId}")
    @ResponseBody
    public List<ChatMessage> messageInfoGET(@PathVariable String roomId) {
        if(chatService.checkAccess(roomId))
        {
            return chatService.findMessages(roomId);
        }
        return null;
    }

    //게시판 내용 메시지 보내기
    @GetMapping("/message/orderItem")
    @ResponseBody
    public void orderItemGET(@RequestParam("bid") Long bid, @RequestParam("userId") Long id, Principal principal, HttpServletResponse response) throws IOException {
        BoardDTO boardDTO = boardService.getBoardDetail(bid);
        String roomId = chatService.createChatRoom(boardDTO.getWriter(), principal);
        chatService.convertBoardToMessage(boardDTO, id, principal, roomId);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('관심있는 게시물을 전송했습니다.');location.href='/chat/room/enter/"+roomId+"';</script>");
        out.flush();
    }

}
