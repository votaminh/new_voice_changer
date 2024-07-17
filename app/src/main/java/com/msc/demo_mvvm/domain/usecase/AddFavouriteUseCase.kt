package com.msc.demo_mvvm.domain.usecase

import com.msc.demo_mvvm.data.repositories.FavouriteSoundRepository
import com.msc.demo_mvvm.domain.layer.DetailsSound
import javax.inject.Inject

class AddFavouriteUseCase @Inject constructor(val favouriteSoundRepository: FavouriteSoundRepository) : UseCase<AddFavouriteUseCase.Param, Long>() {
    class Param(val detailsSound: DetailsSound) : UseCase.Param()

    override suspend fun execute(param: Param): Long {
        return favouriteSoundRepository.insert(param.detailsSound)
    }
}