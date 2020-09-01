package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.dto.WalletDTO;
import com.voting.model.request.VotingRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.TransactionResponse;
import com.voting.model.response.VotingResponse;
import com.voting.service.ITransactionService;
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
public class TransactionController {
    private final Logger logger = LogManager.getLogger(TransactionController.class);
    private static final Gson PARSER = new Gson();

    private ITransactionService transactionService;
    private IWalletService walletService;

    @Autowired
    public TransactionController(ITransactionService transactionService
            , IWalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @PostMapping(value = "/voting", produces = "application/json;charset=utf8")
    public ResponseEntity<String> voting(@RequestBody VotingRequest request) {
        String logId = request.getRequestId();
        logger.info("{}| Request data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(logId);
            if (!request.isValidData()) {
                logger.warn("{}| Validate request voting: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
            }
            logger.info("{}| Valid data request voting success!", logId);

            WalletDTO walletDTO = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            if (request.getReceiverWallet().equals(walletDTO.getWalletId())) {
                logger.warn("{}| Sender - {} can't voting!", logId, walletDTO.getWalletId());
                response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
            }

            VotingResponse responseBody = transactionService.voting(logId, request, walletDTO);

            if (StringUtils.isBlank(responseBody.getTransId())) {
                logger.warn("{}| Voting fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("{}| Voting success with trans id: {}", logId, responseBody.getTransId());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request voting catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/get-transactions", "/get-transactions/{walletId}"}, produces = "application/json;charset=utf8")
    public ResponseEntity<String> getTransaction(@PathVariable(required = false) String walletId) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: walletId - {}", logId, walletId);
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(logId);

            WalletDTO rootWallet = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if (StringUtils.isBlank(walletId)) {
                walletId = rootWallet.getWalletId();
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
            List<TransactionResponse> responseData = transactionService.getTransactions(logId, walletId, rootWallet.getWalletId());
            JsonObject responseBody = new JsonObject().put("transactions", responseData);
            if (responseData == null) {
                logger.warn("{}| Get transactions fail: {}", logId, responseBody.toString());
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, logId, responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (responseData.size() <= 0) {
                logger.warn("{}| Transaction not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, logId, responseBody.toString());
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Get transactions success with size: {}", logId, responseData.size());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, logId, responseBody.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("{}| Request Get transactions catch exception: ", logId, ex);
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
