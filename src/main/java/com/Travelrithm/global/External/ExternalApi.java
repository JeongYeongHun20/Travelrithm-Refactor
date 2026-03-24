package com.Travelrithm.global.External;

public interface ExternalApi {
    <T> T executeGet(GetApiRequest request, Class<T> responseType);
    <T> T executePost(PostApiRequest request, Class<T> responseType);
    <T> T executeDelete(DeleteApiRequest request, Class<T> responseType);
}
