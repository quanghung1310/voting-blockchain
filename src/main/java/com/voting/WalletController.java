package com.voting;

import com.google.gson.Gson;
import com.voting.constants.ErrorConstant;
import com.voting.constants.StringConstant;
import com.voting.model.request.LogInRequest;
import com.voting.model.request.RegisterRequest;
import com.voting.model.response.BaseResponse;
import com.voting.model.response.RegisterResponse;
import com.voting.service.IWalletService;
import com.voting.util.DataUtil;
import com.voting.util.JwtUtil;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class WalletController {
    private final Logger logger = LogManager.getLogger(WalletController.class);

    private static final Gson PARSER = new Gson();

    private AuthenticationManager authenticationManager;

    private IWalletService walletService;

    private JwtUtil jwtUtil;

    private JavaMailSender javaMailSender;

    @Autowired
    public WalletController(AuthenticationManager authenticationManager
            , IWalletService walletService
            , JwtUtil jwtUtil
            , JavaMailSender javaMailSender) {
        this.authenticationManager = authenticationManager;
        this.walletService = walletService;
        this.jwtUtil = jwtUtil;
        this.javaMailSender = javaMailSender;
    }

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
            if (responseBody == null) {
                logger.warn("{}| Register fail", logId);
                response = DataUtil.buildResponse(ErrorConstant.SYSTEM_ERROR, request.getRequestId(), null);
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
            logger.info("{}| Register success with wallet id: {}", logId, responseBody.getWalletId());

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(request.getEmail());
            msg.setSubject("Your Account VOTING-SYSTEM");
            String body = "Hello " + request.getFirstName() + " " + request.getLastName();
            body += ",\nWallet Id: " + responseBody.getWalletId();
            body += ",\nWallet Address: " + responseBody.getWalletAddress();
            body += "\nWallet Primary: " + responseBody.getWalletPrimary();
            body += "\nCreate date: " + DataUtil.convertTimeWithFormat(response.getResponseTime(), StringConstant.FORMAT_ddMMyyyyTHHmmss);
            body += "\nThanks you,\nThe VotingSystem Team";
            msg.setText(body);

            javaMailSender.send(msg);

            response = DataUtil.buildResponse(ErrorConstant.SUCCESS, request.getRequestId(), responseBody.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("{}| Request register catch exception: ", logId, ex);
            response = DataUtil.buildResponse(ErrorConstant.BAD_FORMAT_DATA, request.getRequestId(),null);
            return new ResponseEntity<>(
                    response.toString(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/login", produces = "application/json;charset=utf8")
    public @ResponseBody ResponseEntity<String> login(@RequestBody LogInRequest authRequest) {
        String logId = DataUtil.createRequestId();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            return new ResponseEntity<>("BAD REQUEST DATA", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new JsonObject()
                .put("bearerToken", jwtUtil.generateToken(authRequest.getEmail()))
                .toString(), HttpStatus.OK);
    }


}
