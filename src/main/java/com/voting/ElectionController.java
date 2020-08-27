package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.dto.ElectorDTO;
import com.voting.dto.WalletDTO;
import com.voting.model.request.ElectorRequest;
import com.voting.model.request.NewVoteContent;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ElectionController {
    private final Logger logger = LogManager.getLogger(ElectionController.class);
    private static final Gson PARSER = new Gson();

    private IVoteContentService voteContentService;

    private IWalletService walletService;

    @Autowired
    public ElectionController(IVoteContentService voteContentService
            , IWalletService walletService) {
        this.voteContentService = voteContentService;
        this.walletService = walletService;
    }

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

            WalletDTO walletDTO = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            VoteContentResponse contentVote = voteContentService.createContentVote(logId, request, walletDTO.getWalletId());
            if (contentVote == null) {
                logger.warn("{}| Create content vote fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Response to client: {}", logId, contentVote.toString());

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), contentVote.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request create content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/get-vote-content", "/get-vote-content/{startDate}/{endDate}"}, produces = "application/json;charset=utf8")
    public ResponseEntity<String> getContent(@PathVariable(required = false) String startDate,
                                             @PathVariable(required = false) String endDate) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: startDate - {}, endDate - {}", logId, startDate, endDate);
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(logId);

            List<VoteContentResponse> contents = voteContentService.getContent(logId, startDate, endDate);
            JsonObject responseData = new JsonObject().put("contents", contents);
            if (contents == null) {
                logger.warn("{}| Get vote content fail!!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, logId, responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);
                return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
            }

            if (contents.size() <= 0) {
                logger.warn("{}| Vote content not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, logId, responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, logId, responseData.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Get content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, logId,null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/get-elector", "/get-elector/{contentId}"}, produces = "application/json;charset=utf8")
    public ResponseEntity<String> getElector(@PathVariable(required = false) String contentId) {
        String logId = DataUtil.createRequestId();
        logger.info("{}| Request data: contentId - {}", logId, contentId);
        BaseResponse response;
        try {
            List<ElectorResponse> electors = walletService.getElector(logId, contentId);
            JsonObject responseData = new JsonObject().put("electors", electors);
            if (electors == null) {
                logger.warn("{}| Get elector fail!!", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, logId, responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);
                return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);

            }

            if (electors.size() <= 0) {
                logger.warn("{}| Elector not found!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, logId, responseData.toString());
                logger.info("{}| Response to client: {}", logId, response);

                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, logId, responseData.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Get content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, logId,null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/register-elector"}, produces = "application/json;charset=utf8")
    public ResponseEntity<String> getElector(@RequestBody ElectorRequest request) {
        String logId = request.getRequestId();
        logger.info("{}| Request register elector data: {}", logId, PARSER.toJson(request));
        BaseResponse response = new BaseResponse();
        try {
            response.setRequestId(request.getRequestId());
            if (!request.isValidData()) {
                logger.warn("{}| Validate request register elector: Fail!", logId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Valid data request register elector success!", logId);

            WalletDTO walletDTO = getWallet(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            String walletId = request.getWalletId();
            if (StringUtils.isBlank(walletId)) {
                walletId = walletDTO.getWalletId();
            }

            WalletDTO dto = walletService.findByWalletId(walletId);

            if (dto == null) {
                logger.warn("{}| Wallet id - {} not found!", logId, walletId);
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }

            ElectorDTO electorDTO = new ElectorDTO();
            electorDTO.setContentId(request.getContentId());
            electorDTO.setWalletId(walletId);

            ElectorResponse elector = walletService.saveElector(logId, dto, electorDTO);

            if (elector == null) {
                logger.warn("{}| Save elector with wallet id - {}: Fail!", logId, electorDTO.getWalletId());
                response = DataUtil.buildResponse(ErrorConstant.NOT_EXISTED, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), elector.toString());
            logger.info("{}| Response to client: {}", logId, response);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Get content vote catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(), null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private WalletDTO getWallet(Object principal) {
        return walletService.findByEmail(((UserDetails) principal).getUsername());
    }

}
