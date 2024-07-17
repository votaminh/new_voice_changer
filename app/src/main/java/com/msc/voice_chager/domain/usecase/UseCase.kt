package com.msc.voice_chager.domain.usecase

abstract class UseCase<in P : UseCase.Param, out T> where T : Any {

    abstract suspend fun execute(param: P): T

    open class Param
}