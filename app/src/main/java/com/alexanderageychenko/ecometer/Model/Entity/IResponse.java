package com.alexanderageychenko.ecometer.Model.Entity;

import java.util.List;

public interface IResponse {
    List<Error> getError();
    Long getDate();
    Integer getHttpCode();
}
