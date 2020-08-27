package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.BlockRequest;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.request.TransactionRequest;
import com.voting.model.response.*;
import com.voting.service.IBlockService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
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
public class MineTransactionController {
    private final Logger logger = LogManager.getLogger(MineTransactionController.class);
    private static final Gson PARSER = new Gson();

    public IBlockService blockService;

    @Autowired
    public MineTransactionController(IBlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping(value = "/mine-transaction", produces = "application/json;charset=utf8")
    public ResponseEntity<String> mineTransaction(@RequestBody MineTransactionRequest request) {
        String logId = request.getRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request mine transaction: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
            }
            logger.info("{}| Valid data request mine transaction success!", logId);

            JsonObject responseData = blockService.mineTransaction(logId, request);
            int resultCode = responseData.getInteger("resultCode", -1);
            MineTransactionResponse transactionResponse = PARSER.fromJson(responseData.getString("transactionResponse", MineTransactionResponse.builder().build().toString()), MineTransactionResponse.class);
            if (resultCode != 0) {
                logger.warn("{}| Mine transaction fail with error - {}", logId, resultCode);
                response = DataUtil.buildResponse(resultCode, request.getRequestId(), transactionResponse.toString());
                logger.info("{}| Response to client: {}", logId, response);
                return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response = DataUtil.buildResponse(resultCode, request.getRequestId(), transactionResponse.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Mine transaction catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/get-blocks", produces = "application/json;charset=utf8")
    public ResponseEntity<String> getBlock(@RequestBody BlockRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request get blocks: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request get blocks success!", logId);

            List<BlockResponse> responseData = blockService.getBlocks(logId, request);
            JsonObject responseBody = new JsonObject().put("blocks", responseData);
            if (responseData == null) {
                logger.warn("{}| Get blocks fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            if (responseData.size() <= 0) {
                logger.warn("{}| Blocks not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Get blocks success with size: {}", logId, responseBody.toString());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request get blocks catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
