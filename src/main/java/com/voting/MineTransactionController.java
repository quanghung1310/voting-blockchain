package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.MineTransactionResponse;
import com.voting.model.response.TransactionResponse;
import com.voting.model.response.VoteContentResponse;
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

    @Autowired
    public IBlockService blockService;

    @PostMapping(value = "/mine-transaction", produces = "application/json;charset=utf8")
    public ResponseEntity<String> mineTransaction(@RequestBody MineTransactionRequest request) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request mine transaction: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request mine transaction success!", logId);

            JsonObject responseData = blockService.mineTransaction(logId, request);
            int resultCode = responseData.getInteger("resultCode", -1);
            MineTransactionResponse transactionResponse = PARSER.fromJson(responseData.getString("transactionResponse", MineTransactionResponse.builder().build().toString()), MineTransactionResponse.class);
            if (resultCode != 0) {
                logger.warn("{}| Mine transaction fail with error - {}", logId, resultCode);
                response = DataUtil.buildResponse(resultCode, request.getRequestId(), transactionResponse.toString());
                logger.info("{}| Response to client: {}", logId, response);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = DataUtil.buildResponse(resultCode, request.getRequestId(), transactionResponse.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Mine transaction catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.OK);
            return responseEntity;
        }
    }
}
