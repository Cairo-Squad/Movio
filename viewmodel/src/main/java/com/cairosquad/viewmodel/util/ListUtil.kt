package com.cairosquad.viewmodel.util

fun <T> combineTwoList(
    list1: List<T>,
    list2: List<T>,
): List<T> {
    val mergedList = mutableListOf<T>()
    val iterator1 = list1.iterator()
    val iterator2 = list2.iterator()

    while (iterator1.hasNext() || iterator2.hasNext()) {
        if (iterator1.hasNext()) mergedList.add(iterator1.next())
        if (iterator2.hasNext()) mergedList.add(iterator2.next())
    }
    return mergedList
}