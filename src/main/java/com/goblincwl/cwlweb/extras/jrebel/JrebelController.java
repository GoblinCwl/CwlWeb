package com.goblincwl.cwlweb.extras.jrebel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.goblincwl.cwlweb.extras.jrebel.entity.JrebelInfoEntity;
import com.goblincwl.cwlweb.extras.jrebel.entity.XMLResponse;
import com.goblincwl.cwlweb.extras.jrebel.util.jrebel.JrebelSign;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Jrebel 热部署激活 Controller
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 10:58
 */
@RestController
@RequestMapping("/extras/jrebel")
@RequiredArgsConstructor
public class JrebelController {

    @RequestMapping({"/jrebel/leases", "/agent/leases"})
    public String jrebelLeases(JrebelInfoEntity jrebelInfoEntity) {
        String validFrom = null;
        String validUntil = null;
        boolean offline = Boolean.parseBoolean(jrebelInfoEntity.getOffline());
        if (offline) {
            long clientTimeUntil = Long.parseLong(jrebelInfoEntity.getClientTime()) + 180L * 24 * 60 * 60 * 1000;
            validFrom = jrebelInfoEntity.getClientTime();
            validUntil = String.valueOf(clientTimeUntil);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverVersion", "3.2.4");
        jsonObject.put("serverProtocolVersion", "1.1");
        jsonObject.put("serverGuid", "a1b4aea8-b031-4302-b602-670a990272cb");
        jsonObject.put("groupType", "managed");
        jsonObject.put("id", 1);
        jsonObject.put("licenseType", 1);
        jsonObject.put("evaluationLicense", false);
        jsonObject.put("signature", "OJE9wGg2xncSb+VgnYT+9HGCFaLOk28tneMFhCbpVMKoC/Iq4LuaDKPirBjG4o394/UjCDGgTBpIrzcXNPdVxVr8PnQzpy7ZSToGO8wv/KIWZT9/ba7bDbA8/RZ4B37YkCeXhjaixpmoyz/CIZMnei4q7oWR7DYUOlOcEWDQhiY=");
        jsonObject.put("serverRandomness", "H2ulzLlh7E0=");
        jsonObject.put("seatPoolType", "standalone");
        jsonObject.put("statusCode", "SUCCESS");
        jsonObject.put("offline", offline);
        jsonObject.put("validFrom", validFrom);
        jsonObject.put("validUntil", validUntil);
        jsonObject.put("company", "Administrator");
        jsonObject.put("orderId", "");
        jsonObject.put("zeroIds", new JSONArray[0]);
        jsonObject.put("licenseValidFrom", 1490544001000L);
        jsonObject.put("licenseValidUntil", 1691839999000L);
        if (jrebelInfoEntity.getRandomness() == null || jrebelInfoEntity.getUsername() == null || jrebelInfoEntity.getGuid() == null) {
            return null;
        } else {
            JrebelSign jrebelSign = new JrebelSign();
            jrebelSign.toLeaseCreateJson(jrebelInfoEntity.getRandomness(), jrebelInfoEntity.getGuid(), offline, validFrom, validUntil);
            String signature = jrebelSign.getSignature();
            jsonObject.put("signature", signature);
            jsonObject.put("company", jrebelInfoEntity.getUsername());
            return jsonObject.toJSONString();
        }
    }

    @RequestMapping({"/jrebel/leases/1", "/agent/leases/1"})
    public String jrebelLeases1(JrebelInfoEntity jrebelInfoEntity) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverVersion", "3.2.4");
        jsonObject.put("serverProtocolVersion", "1.1");
        jsonObject.put("serverGuid", "a1b4aea8-b031-4302-b602-670a990272cb");
        jsonObject.put("groupType", "managed");
        jsonObject.put("statusCode", "SUCCESS");
        jsonObject.put("msg", null);
        jsonObject.put("statusMessage", null);
        String username = jrebelInfoEntity.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            jsonObject.put("company", username);
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping("/jrebel/validate-connection")
    public String validateConnection() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverVersion", "3.2.4");
        jsonObject.put("serverProtocolVersion", "1.1");
        jsonObject.put("serverGuid", "a1b4aea8-b031-4302-b602-670a990272cb");
        jsonObject.put("groupType", "managed");
        jsonObject.put("statusCode", "SUCCESS");
        jsonObject.put("company", "Administrator");
        jsonObject.put("canGetLease", true);
        jsonObject.put("licenseType", 1);
        jsonObject.put("evaluationLicense", false);
        jsonObject.put("seatPoolType", "standalone");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/rpc/ping.action", produces = MediaType.APPLICATION_XML_VALUE)
    public XMLResponse rpcPing(String salt) {
        XMLResponse response = new XMLResponse();
        if (StringUtils.isNotEmpty(salt)) {
            response.setMessage("");
            response.setResponseCode("OK");
            response.setSalt(salt);
        }
        return response;
    }

    @RequestMapping(value = "/rpc/obtainTicket.action", produces = MediaType.APPLICATION_XML_VALUE)
    public XMLResponse rpcObtainTicket(String salt, String userName) {
        XMLResponse response = new XMLResponse();
        String prolongationPeriod = "607875500";
        if (!StringUtils.isEmpty(salt) && !StringUtils.isEmpty(userName)) {
            response.setMessage("");
            response.setProlongationPeriod(prolongationPeriod);
            response.setResponseCode("OK");
            response.setTicketId("1");
            response.setSalt(salt);
            response.setTicketProperties("licensee=" + userName + "\tlicenseType=0");
        }
        return response;
    }

    @RequestMapping(value = "/rpc/releaseTicket.action", produces = MediaType.APPLICATION_XML_VALUE)
    public XMLResponse rpcReleaseTicket(String salt) {
        XMLResponse response = new XMLResponse();
        if (StringUtils.isNotEmpty(salt)) {
            response.setMessage("");
            response.setResponseCode("OK");
            response.setSalt(salt);
        }
        return response;
    }
}
