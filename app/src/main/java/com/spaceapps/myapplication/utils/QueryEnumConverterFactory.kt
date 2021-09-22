package com.spaceapps.myapplication.utils

import com.squareup.moshi.Json
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

object QueryEnumConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type is Class<*> && type.isEnum) {
            return Converter<Enum<*>, String> { it.getJsonName() }
        }
        return super.stringConverter(type, annotations, retrofit)
    }

    private fun Enum<*>.getJsonName(): String? {
        return try {
            this::class.java.getField(name).getAnnotation(Json::class.java)?.name
        } catch (_: Exception) {
            null
        }
    }
}
