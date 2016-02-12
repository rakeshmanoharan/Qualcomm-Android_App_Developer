//
// Created by Rakesh on 2/11/2016.
//

#include "com_weather_qualcomm_MainActivity.h"
#include <stdlib.h>
#include <stdio.h>
#define DELIMITER ","
JNIEXPORT jstring JNICALL Java_com_weather_1qualcomm_MainActivity_convertTemperature(JNIEnv *env, jobject dummy, jstring inputStr){
    char *pStr = (char *)env->GetStringUTFChars(inputStr, JNI_FALSE);
    char *pOutput = (char*)malloc(30);
    memset(pOutput, 0, sizeof(pOutput));
    char *pToken;
    pToken = (char *) strtok (pStr, DELIMITER);
    while (pToken != NULL)
    {
        /* Convert the String to integer */
        int tempC = atoi(pToken);

        /* Convert the temperature from C to F */
        int tempF = tempC * ((double)9/5) + 32;

        /* add the converted temperature to output buffer */
        int length = strlen(pOutput);
        sprintf(pOutput + length, "%d,", tempF);
        pToken = strtok (NULL, DELIMITER);
    }

    /* Convert the char* to jstring */
    /* return the string */
    return (*env).NewStringUTF(pOutput);
}