package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.dto.WalletDTO;
import com.voting.model.request.MineTransactionRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.BlockResponse;
import com.voting.model.response.MineTransactionResponse;
import com.voting.service.IBlockService;
import com.voting.service.IWalletService;
import com.voting.util.DataUtil;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MineTransactionController {
    private final Logger logger = LogManager.getLogger(MineTransactionController.class);
    private static final Gson PARSER = new Gson();

    public IBlockService blockService;

    private IWalletService walletService;

    @Autowired
    public MineTransactionController(IBlockService blockService, IWalletService walletService) {
        this.blockService = blockService;
        this.walletService = walletService;
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

            WalletDTO walletDTO = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            JsonObject responseData = blockService.mineTransaction(logId, walletDTO, request);
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

    @GetMapping(value = {"/get-blocks", "/get-blocks/{walletId}"}, produces = "application/json;charset=utf8")
    public ResponseEntity<String> getBlock(@PathVariable(required = false) String walletId) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data:walletId - {}", logId, PARSER.toJson(walletId));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(logId);
            if (StringUtils.isBlank(walletId)) {
                WalletDTO walletDTO = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                walletId = walletDTO.getWalletId();
            } else if (walletId.equals("all")) {
                walletId = "";
            } else {
                WalletDTO walletDTO = walletService.findByWalletId(walletId);
                if (walletDTO == null) {
                    logger.warn("{}| Wallet id - {} not existed", logId, walletId);
                    response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, logId, null);
                    return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
                }
            }

            List<BlockResponse> responseData = blockService.getBlocks(logId, walletId);
            JsonObject responseBody = new JsonObject().put("blocks", responseData);
            if (responseData == null) {
                logger.warn("{}| Get blocks fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, logId, responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            if (responseData.size() <= 0) {
                logger.warn("{}| Blocks not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, logId, responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Get blocks success with size: {}", logId, responseBody.toString());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, logId, responseBody.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request get blocks catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, logId,null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private WalletDTO getWallet(Object principal) {
        return walletService.findByEmail(((UserDetails) principal).getUsername());
    }
}
