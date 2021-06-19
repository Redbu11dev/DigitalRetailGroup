package com.vashkpi.digitalretailgroup.data.mappers

interface Mapper<I, O> {
    fun map(input: I): O
}