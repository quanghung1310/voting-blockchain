package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.NewContentVote;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.RegisterResponse;
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

    @PostMapping(value = "/create-content-vote", produces = "application/json;charset=utf8")
    public ResponseEntity<String> createContent(@RequestBody NewContentVote request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request create content vote: Fail!", logId);
                response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request create content vote success!", logId);

            String contentId = transactionService.createContentVote(logId, request);
            JsonObject responseBody = new JsonObject().put("contentId", contentId);
            if (StringUtils.isBlank(contentId)) {
                logger.warn("{}| Create content vote fail: {}", logId, responseBody.toString());
                response = buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Create content vote success with id: {}", logId, responseBody);

            response = buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request create content vote catch exception: ", logId, ex);
            response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }

    @PostMapping(value = "/voting", produces = "application/json;charset=utf8")
    public ResponseEntity<String> voting(@RequestBody VotingRequest request) {

        return null;
    }

    private static BaseResponse buildResponse(int resultCode, String requestId, String responseBody) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResultCode(resultCode);
        baseResponse.setMessage(ErrorConstant.getMessage(resultCode));
        baseResponse.setResponseTime(System.currentTimeMillis());
        baseResponse.setRequestId(requestId);
        if (responseBody != null) {
            baseResponse.setData(new JsonObject(responseBody));
        }

        return baseResponse;
    }
}
