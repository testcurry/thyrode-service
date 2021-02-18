package vip.testops.manager.common;

public enum BaseCodeEnum {

    COMMON_SUCCESS(1000,"success","处理成功"),
    PARAM_MISS(2001,"requeired parameters[%s] are missing","缺少必要参数[%s]"),
    PARAM_ILLEGAL(2002,"requeired parameters[%s] are illegal","参数[%s]不合法"),
    SERVICE_ERROR(3001,"service error:%s","服务异常：%s");

    private final Integer code;
    private final String descEN;
    private final String descCN;

    BaseCodeEnum(Integer code,String descEN,String descCN){
        this.code=code;
        this.descEN=descEN;
        this.descCN=descCN;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescEN() {
        return descEN;
    }

    public String getDescCN() {
        return descCN;
    }

    public static BaseCodeEnum getByCode(Integer code) {
        if (code==null){
            return null;
        }
        BaseCodeEnum target=null;
        BaseCodeEnum[] enums=BaseCodeEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getCode()==code){
                target=enums[i];
            }
            break;
        }
        return target;
    }

}
