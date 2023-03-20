package br.usp.pcs.back;

import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.repository.UserRepository;
import br.usp.pcs.back.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserRepository USER_REPOSITORY = new UserRepository();
    private static final UserDataSource USER_DATASOURCE = new UserDataSource(USER_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static UserDataSource getUserDatasource() {
        return USER_DATASOURCE;
    }

    static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
