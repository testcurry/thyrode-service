package vip.testops.account.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    public static String getSecret(String key){
        //使用AES加密算法获取秘钥
        String secret=null;
        try {
            Key secretKey = AESUtil.generateKey(key);
            secret = AESUtil.encrypt("tyloo-api", secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secret;
    }

    public static String createToken(Map<String,Object> claim,String secret,int expire){
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,expire);
        Date expireDate = nowTime.getTime();
        HashMap<String, Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        return JWT.create().withHeader(map)
                .withClaim("userInfo",claim)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secret));
    }

    //token校验的方法，此项目用不动，作为一个演示功能使用
    public static Claim verifyToken(String token,String secret){
        JWTVerifier verifier= JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw  new RuntimeException("token is expire");
        }
        return jwt.getClaim("userInfo");
    }

//    public static void main(String[] args) {
//        String secret="curry secret";
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("userId",10);
//        claims.put("userName","curry");
//        claims.put("isAdmin",true);
//        String token = createToken(claims, secret, 1);
//        System.out.println(token);
//        Claim claim = verifyToken(token, secret);
//        System.out.println(claim.asMap());
//    }
}
