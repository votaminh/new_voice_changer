package com.msc.voice_chager.domain.usecase

import com.msc.voice_chager.data.repositories.FavouriteSoundRepository
import com.msc.voice_chager.domain.layer.DetailsSound
import javax.inject.Inject

class UnFavouriteUseCase @Inject constructor(val favouriteSoundRepository: FavouriteSoundRepository) : UseCase<UnFavouriteUseCase.Param, Int>() {
    class Param(val detailsSound: DetailsSound) : UseCase.Param()

    override suspend fun execute(param: Param): Int {
        return favouriteSoundRepository.remove(param.detailsSound)
    }
}