package com.cairosquad.viewmodel.more

import com.cairosquad.entity.Language

fun MoreScreenState.Theme.toDomain(): com.cairosquad.entity.Theme {
    return when (this) {
        MoreScreenState.Theme.LIGHT -> com.cairosquad.entity.Theme.LIGHT
        MoreScreenState.Theme.DARK -> com.cairosquad.entity.Theme.DARK
    }
}

 fun Language.toMoreScreenStateLanguage(): MoreScreenState.Language {
    return MoreScreenState.Language(code = this.code, name = this.name)
}

 fun MoreScreenState.Language.toDomainLanguage(): Language {
    return Language(code = this.code, name = this.name)
}
