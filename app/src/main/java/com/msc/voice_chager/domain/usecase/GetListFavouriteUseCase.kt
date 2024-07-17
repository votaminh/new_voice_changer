package com.msc.voice_chager.domain.usecase

import com.msc.voice_chager.data.repositories.FavouriteSoundRepository
import com.msc.voice_chager.domain.layer.DetailsSound
import javax.inject.Inject

class GetListFavouriteUseCase @Inject constructor(private val favouriteSoundRepository: FavouriteSoundRepository)
    : UseCase<GetListFavouriteUseCase.Param, List<DetailsSound>>() {
    class Param : UseCase.Param()

    override suspend fun execute(param: Param): List<DetailsSound> {
        return favouriteSoundRepository.getList()
    }
}