package com.sukajee.itemlist.data.mappers

import com.sukajee.itemlist.data.remote.ItemDto
import com.sukajee.itemlist.domain.model.Item

fun ItemDto.toItem(): Item {
    return Item(
        itemId = id,
        itemListId = listId,
        itemName = name
    )
}

fun Item.toItemDto(): ItemDto {
    return ItemDto(
        id = itemId,
        listId = itemListId,
        name = itemName
    )
}