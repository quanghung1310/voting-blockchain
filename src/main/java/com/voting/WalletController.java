package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.*;
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
                logger.warn("{}| Validate request register data: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request register success!", logId);

            RegisterResponse responseBody = walletService.register(logId, request);
            if (StringUtils.isBlank(responseBody.getWalletId())) {
                logger.warn("{}| Register fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Register success with wallet id: {}", logId, responseBody.getWalletId());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request register catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
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
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request login success!", logId);

            LogInResponse responseBody = walletService.login(logId, request);

            if (responseBody == null) {
                logger.warn("{}| Login fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            if (StringUtils.isBlank(responseBody.getEmail())) {
                logger.warn("{}| Username/password wrong!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            logger.info("{}| Response to client: {}", logId, response.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request login catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }


}
