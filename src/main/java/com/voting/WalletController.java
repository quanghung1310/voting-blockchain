package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.LogInRequest;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.LogInResponse;
import com.voting.model.response.RegisterResponse;
import com.voting.service.IWalletService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class WalletController {
    private final Logger logger = LogManager.getLogger(WalletController.class);

    private static final Gson PARSER = new Gson();

    @Autowired
    private IWalletService walletService;

    @PostMapping(value = "/register", produces = "application/json;charset=utf8")
    public @ResponseBody ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request add new wallet data: Fail!", logId);
                response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request add new wallet success!", logId);

            RegisterResponse responseBody = walletService.register(logId, request);
            if (StringUtils.isBlank(responseBody.getWalletId())) {
                logger.warn("{}| Add new wallet fail: {}", logId, responseBody.toString());
                response = buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Add new wallet success with wallet id: {}", logId, responseBody.getWalletId());

            response = buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request add new wallet catch exception: ", logId, ex);
            response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }

    @PostMapping(value = "/login", produces = "application/json;charset=utf8")
    public @ResponseBody ResponseEntity<String> login(@RequestBody LogInRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request login: Fail!", logId);
                response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request login success!", logId);

            LogInResponse responseBody = walletService.login(logId, request);

            if (responseBody == null) {
                logger.warn("{}| Login fail!", logId);
                response = buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request login catch exception: ", logId, ex);
            response = buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
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
