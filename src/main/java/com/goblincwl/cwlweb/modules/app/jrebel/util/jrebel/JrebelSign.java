package com.goblincwl.cwlweb.modules.app.jrebel.util.jrebel;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ☪wl
 */
public class JrebelSign {
    private String signature;

    public void toLeaseCreateJson(String clientRandomness, String guid, boolean offline, String validFrom, String validUntil) {
        //服务端随机数,如果要自己生成，务必将其写到json的serverRandomness中
        String serverRandomness = "H2ulzLlh7E0=";
        String s2;
        if (offline) {
            s2 = StringUtils.join(new String[]{clientRandomness, serverRandomness, guid, String.valueOf(offline), validFrom, validUntil}, ';');
        } else {
            s2 = StringUtils.join(new String[]{clientRandomness, serverRandomness, guid, String.valueOf(offline)}, ';');
        }
        final byte[] a2 = LicenseServer2ToJrebelPrivateKey.a(s2.getBytes());
        this.signature = ByteUtil.a(a2);
    }

    public String getSignature() {
        return signature;
    }

}
