package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.ElectorRequest;
import com.voting.model.request.NewVoteContent;
import com.voting.model.request.VoteContentRequest;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.ElectorResponse;
import com.voting.model.response.VoteContentResponse;
import com.voting.service.IVoteContentService;
import com.voting.service.IWalletService;
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

import java.util.List;

@RestController
public class ElectionController {
    private final Logger logger = LogManager.getLogger(ElectionController.class);
    private static final Gson PARSER = new Gson();

    @Autowired
    private IVoteContentService voteContentService;
    @Autowired
    private IWalletService walletService;

    @PostMapping(value = "/create-vote-content", produces = "application/json;charset=utf8")
    public ResponseEntity<String> createContent(@RequestBody NewVoteContent request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request create content vote: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request create content vote success!", logId);

            String contentId = voteContentService.createContentVote(logId, request);
            JsonObject responseBody = new JsonObject().put("contentId", contentId);
            if (StringUtils.isBlank(contentId)) {
                logger.warn("{}| Create content vote fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Create content vote success with id: {}", logId, responseBody);

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            response.setData(new JsonObject(responseBody.toString()));
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request create content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }

    @PostMapping(value = "/get-vote-content", produces = "application/json;charset=utf8")
    public ResponseEntity<String> getContent(@RequestBody VoteContentRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request create content vote: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request get content vote success!", logId);

            List<VoteContentResponse> contents = voteContentService.getContent(logId, request);
            JsonObject responseData = new JsonObject().put("contents", contents);
            if (contents == null) {
                logger.warn("{}| Get vote content fail!!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

            }
            if (contents.size() <= 0) {
                logger.warn("{}| Vote content not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseData.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Get content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }

    @PostMapping(value = "/get-elector", produces = "application/json;charset=utf8")
    public ResponseEntity<String> getElector(@RequestBody ElectorRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request get elector: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request get elector success!", logId);

            List<ElectorResponse> electors = walletService.getElector(logId, request);
            JsonObject responseData = new JsonObject().put("electors", electors);
            if (electors == null) {
                logger.warn("{}| Get elector fail!!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

            }

            if (electors.size() <= 0) {
                logger.warn("{}| Elector not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseData.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Get content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }
}
