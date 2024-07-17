package com.msc.demo_mvvm.domain.usecase

import com.msc.demo_mvvm.data.repositories.FavouriteSoundRepository
import com.msc.demo_mvvm.domain.layer.DetailsSound
import javax.inject.Inject

class UnFavouriteUseCase @Inject constructor(val favouriteSoundRepository: FavouriteSoundRepository) : UseCase<UnFavouriteUseCase.Param, Int>() {
    class Param(val detailsSound: DetailsSound) : UseCase.Param()

    override suspend fun execute(param: Param): Int {
        return favouriteSoundRepository.remove(param.detailsSound)
    }
}