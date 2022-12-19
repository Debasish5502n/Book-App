package com.example.bookapp.Model

class ParentModel (var title: String,var childModelClass : List<ChildModel>) {

    constructor() : this("", emptyList())

}