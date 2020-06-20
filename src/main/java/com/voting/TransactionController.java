package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.VotingResponse;
import com.voting.service.ITransactionService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final Logger logger = LogManager.getLogger(TransactionController.class);
    private static final Gson PARSER = new Gson();

    @Autowired
    public ITransactionService transactionService;
    @PostMapping(value = "/voting", produces = "application/json;charset=utf8")
    public ResponseEntity<String> voting(@RequestBody VotingRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request voting: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request voting success!", logId);

            VotingResponse responseBody = transactionService.voting(logId, request);

            if (StringUtils.isBlank(responseBody.getTransId())) {
                logger.warn("{}| Voting fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Voting success with trans id: {}", logId, responseBody.getTransId());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request voting catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }
}
