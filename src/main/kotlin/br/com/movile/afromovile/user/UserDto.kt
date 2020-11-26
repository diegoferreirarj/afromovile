package br.com.movile.afromovile.user

data class UserDto (
    val id: Long? = null,
    val name: String,
    val email: String
)

fun UserDto.toEntity() = User(this.id, this.name, this.email)

fun User.toDto() = UserDto(this.id, this.name, this.email)
