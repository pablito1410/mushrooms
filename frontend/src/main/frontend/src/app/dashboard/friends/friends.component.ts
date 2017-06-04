import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {MdDialog} from "@angular/material";
import {SearchFriendsComponent} from "./search-friends/search-friends.component";
import {FriendDetailsComponent} from "./friend-details/friend-details.component";

@Component({
    moduleId: module.id,
    selector: 'friends-cmp',
    templateUrl: 'friends.component.html'
})
export class FriendsComponent implements OnInit {
    model: any = {};
    users: any[];
    selectedOption: string;

    constructor(public dialog: MdDialog) {}

    ngOnInit(){
        this.users = [
            {username : 'mati'},
            {username : 'bogdan'},
            {username : 'marian'},
            {username : 'kasia'},
            {username : 'grzybiarz'},
            {username : 'muchomor'},
            {username : 'przemo'},
            {username : 'zbigniew'},
            {username : 'roman'},
            {username : 'jez'},
            {username : 'szybki'},
            {username : 'zbieracz'},
            {username : 'fajny'},
            {username : 'podgrzybek'},
            {username : 'grzyb'},
            {username : 'kurka'}
        ]
    }

    openSearchFriendsDialog() {
        let dialogRef = this.dialog.open(SearchFriendsComponent, {
            data: this.users,
            hasBackdrop: true,
            height: '80%',
            width: '80%',
        });
        dialogRef.afterClosed().subscribe(result => {
            this.selectedOption = result;
        });
    }

    openFriendDetailsDialog() {
        let dialogRef = this.dialog.open(FriendDetailsComponent, {
            hasBackdrop: true,
            height: '80%',
            width: '80%',
        });
        dialogRef.afterClosed().subscribe(result => {
            this.selectedOption = result;
        });
    }
}