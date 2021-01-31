package com.spaceapps.myapplication.models

import androidx.annotation.StringDef

@StringDef(value = [CREATED, ID], open = false)
annotation class PostsSorting

const val CREATED = "created"
const val ID = "id"
