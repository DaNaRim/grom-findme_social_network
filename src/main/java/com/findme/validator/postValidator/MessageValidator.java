package com.findme.validator.postValidator;

import com.findme.exception.BadRequestException;

import java.util.regex.Pattern;

public class MessageValidator extends PostValidator {

    private static final Pattern urlPattern = Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$");

    @Override
    public void checkParams(PostValidatorParams params) throws BadRequestException {

        if (params.getMessage() == null) {
            throw new BadRequestException("Message are required field");

        } else if (params.getMessage().length() > 200) {
            throw new BadRequestException("Message length must be less than 200 characters");

        } else { // checking for contains url
            for (String str : params.getMessage().split(" ")) {
                if (urlPattern.matcher(str).find()) {
                    throw new BadRequestException("Message can`t contain url");
                }
            }
        }
    }
}
