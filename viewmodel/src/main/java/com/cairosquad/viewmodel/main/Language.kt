package com.cairosquad.viewmodel.main

data class Language(
    val code: String,
    val name: String
)
//create mappers from and to domain
fun Language.toDomain(): com.cairosquad.entity.Language {
    return com.cairosquad.entity.Language(
        code = code,
        name = name
    )
}
fun com.cairosquad.entity.Language.toUi(): Language {
    return Language(
        code = code,
        name = name
    )
}

