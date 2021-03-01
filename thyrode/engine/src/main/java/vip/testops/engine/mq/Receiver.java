package vip.testops.engine.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.testops.engine.common.Response;
import vip.testops.engine.entity.vto.CaseVTO;
import vip.testops.engine.entity.vto.ExecResultVTO;
import vip.testops.engine.entity.vto.ExecutionVTO;
import vip.testops.engine.enums.SuiteStatus;
import vip.testops.engine.mapper.ExecResultMapper;

import java.util.List;

@Component
@Slf4j
@RabbitListener(
        bindings = @QueueBinding(
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.DIRECT),
                key = "${ma.config.routing-key}",
                value = @Queue(value = "${mq.config.queue-name}", autoDelete = "false")
        )
)
public class Receiver {

    private ExecResultMapper execResultMapper;

    @Autowired
    public void setExecResultMapper(ExecResultMapper execResultMapper) {
        this.execResultMapper = execResultMapper;
    }

    @RabbitHandler
    public void process(String msg) {
        log.info("recive a execution plan --> {}",msg);

        //处理执行计划
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExecutionVTO exectionVTO = objectMapper.readValue(msg, ExecutionVTO.class);
            List<CaseVTO> caseList = exectionVTO.getCaseList();
            caseList.forEach(caseVTO -> {
                //执行具体的case
                Response<ExecResultVTO> response = new Response<>();
                ExecResultVTO execResultVTO = response.getData();
                execResultMapper.addExecResult(execResultVTO);

                //将case的执行结果回写到t_suite表中，通过接口调用方式
                Integer status=SuiteStatus.getByValue(execResultVTO.getStatus()).getKey();



            });
        } catch (Exception e) {
            log.error("json to object error while parsing execution plan",e);

        }
        //将整个project执行结果回写到t_project表中，通过接口调用方式



    }
}
