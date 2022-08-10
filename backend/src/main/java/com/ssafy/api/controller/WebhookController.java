package com.ssafy.api.controller;

import com.ssafy.DTO.openvidu.OVParticipantJoinedDTO;
import com.ssafy.DTO.openvidu.OVParticipantLeftDTO;
import com.ssafy.DTO.openvidu.OVSessionCreatedDTO;
import com.ssafy.DTO.openvidu.OVSessionDestroyedDTO;
import com.ssafy.api.request.openvidu.*;
import com.ssafy.api.service.WebhookService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.Conferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@Slf4j
@RequestMapping("/openvidu_webhook")
public class WebhookController {

    @Autowired
    WebhookService webhookService;

    @PostMapping()
    public ResponseEntity<BaseResponseBody> getEvents(@RequestBody OVAllInOneReq req) {

        log.info("웹훅발생");
        log.info(req.toString());

        String event = req.getEvent();
        Conferences conference = null;
        switch (event) {
            case "sessionCreated":
                OVSessionCreatedDTO ovSessionCreatedDTO = makeSessionCreatedDTO(req);
                conference = webhookService.makeConferenceWithOvSessionCreatedReq(ovSessionCreatedDTO);
                break;
            case "sessionDestroyed":
                OVSessionDestroyedDTO ovSessionDestroyedReq = makeSessionDestroyDTO(req);
                conference = webhookService.editConferenceWithOvSessionDestroyed(ovSessionDestroyedReq);
                break;
            case "participantJoined":
                OVParticipantJoinedDTO ovParticipantJoinedReq = makeParticipantJoinedDTO(req);
                break;
            case "participantLeft":
                OVParticipantLeftDTO ovParticipantLeftDTO = makeParticipantLeftDTO(req);
                break;
            default:
                return ResponseEntity.status(200).body(BaseResponseBody.of(200, "success"));
        }
        // 어떤경우에도 웹훅은 200을 리턴해줘야한다. 그렇지않으면 오픈비두서버가 다음작업을 완료하지않는다고한다.
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "success"));
    }

    public OVSessionCreatedDTO makeSessionCreatedDTO(OVAllInOneReq ovAllInOneReq) {
        OVSessionCreatedDTO ovSessionCreatedDTO = new OVSessionCreatedDTO();

        ovSessionCreatedDTO.setSessionId(ovAllInOneReq.getSessionId());
        ovSessionCreatedDTO.setUniqueSessionId((ovAllInOneReq.getUniqueSessionId()));
        ovSessionCreatedDTO.setEvent(ovAllInOneReq.getEvent());
        // 로컬은 상관없지만 서버와 한국시간은 다르기에 시간을 맞춰주기위해 시간을 더해줌
        ovSessionCreatedDTO.setTimestamp(ovAllInOneReq.getTimestamp() + 32400000);

        return ovSessionCreatedDTO;
    }

    public OVSessionDestroyedDTO makeSessionDestroyDTO(OVAllInOneReq ovAllInOneReq) {
        OVSessionDestroyedDTO ovSessionDestroyedDTO = new OVSessionDestroyedDTO();

        ovSessionDestroyedDTO.setSessionId(ovAllInOneReq.getSessionId());
        ovSessionDestroyedDTO.setUniqueSessionId(ovAllInOneReq.getUniqueSessionId());
        ovSessionDestroyedDTO.setEvent(ovAllInOneReq.getEvent());
        ovSessionDestroyedDTO.setTimestamp(ovAllInOneReq.getTimestamp() + 32400000);
        ovSessionDestroyedDTO.setStartTime(ovAllInOneReq.getStartTime() + 32400000);
        ovSessionDestroyedDTO.setDuration(ovAllInOneReq.getDuration());
        ovSessionDestroyedDTO.setReason(ovAllInOneReq.getReason());

        return ovSessionDestroyedDTO;
    }

    public OVParticipantJoinedDTO makeParticipantJoinedDTO(OVAllInOneReq ovAllInOneReq) {
        OVParticipantJoinedDTO ovParticipantJoinedDTO = new OVParticipantJoinedDTO();

        ovParticipantJoinedDTO.setSessionId(ovAllInOneReq.getSessionId());
        ovParticipantJoinedDTO.setUniqueSessionId(ovAllInOneReq.getUniqueSessionId());
        ovParticipantJoinedDTO.setTimestamp(ovAllInOneReq.getTimestamp() + 32400000);
        ovParticipantJoinedDTO.setParticipantId(ovAllInOneReq.getParticipantId());
        ovParticipantJoinedDTO.setConnectionId(ovAllInOneReq.getConnectionId());
        ovParticipantJoinedDTO.setClientData(ovAllInOneReq.getClientData());
        ovParticipantJoinedDTO.setServerData(ovAllInOneReq.getServerData());
        ovParticipantJoinedDTO.setLocation(ovAllInOneReq.getLocation());
        ovParticipantJoinedDTO.setIp(ovAllInOneReq.getIp());
        ovParticipantJoinedDTO.setPlatform(ovAllInOneReq.getPlatform());
        ovParticipantJoinedDTO.setEvent(ovAllInOneReq.getEvent());

        return ovParticipantJoinedDTO;
    }

    public OVParticipantLeftDTO makeParticipantLeftDTO(OVAllInOneReq ovAllInOneReq) {
        OVParticipantLeftDTO ovParticipantLeftDTO = new OVParticipantLeftDTO();

        ovParticipantLeftDTO.setSessionId(ovAllInOneReq.getSessionId());
        ovParticipantLeftDTO.setUniqueSessionId(ovAllInOneReq.getUniqueSessionId());
        ovParticipantLeftDTO.setTimestamp(ovAllInOneReq.getTimestamp() + 32400000);
        ovParticipantLeftDTO.setParticipantId(ovAllInOneReq.getParticipantId());
        ovParticipantLeftDTO.setConnectionId(ovAllInOneReq.getConnectionId());
        ovParticipantLeftDTO.setClientData(ovAllInOneReq.getClientData());
        ovParticipantLeftDTO.setServerData(ovAllInOneReq.getServerData());
        ovParticipantLeftDTO.setLocation(ovAllInOneReq.getLocation());
        ovParticipantLeftDTO.setIp(ovAllInOneReq.getIp());
        ovParticipantLeftDTO.setPlatform(ovAllInOneReq.getPlatform());
        ovParticipantLeftDTO.setEvent(ovAllInOneReq.getEvent());
        ovParticipantLeftDTO.setReason(ovAllInOneReq.getReason());
        ovParticipantLeftDTO.setDuration(ovAllInOneReq.getDuration());

        return ovParticipantLeftDTO;
    }


}