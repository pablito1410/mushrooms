import { Component, OnInit } from '@angular/core';
import {MapsAPILoader} from "angular2-google-maps/core";

@Component({
    moduleId: module.id,
    selector: 'maps-cmp',
    templateUrl: 'maps.component.html'
})
export class MapsComponent implements OnInit {

    zoom: number = 8;
    lat: number = 50.287977;
    lng: number = 18.677049;
    markers: Marker[];

    constructor(
        private mapsAPILoader: MapsAPILoader) {}

    ngOnInit() {
        this.markers = [
            {
                lat: 51.673858,
                lng: 7.815982,
                label: 'A',
                draggable: true
            },
            {
                lat: 51.373858,
                lng: 7.215982,
                label: 'B',
                draggable: false
            },
            {
                lat: 51.723858,
                lng: 7.895982,
                label: 'C',
                draggable: true
            }
        ];
    }

    clickedMarker(label: string, index: number) {
        console.log(`clicked the marker: ${label || index}`);
    }

    markerDragEnd(m: Marker, $event: MouseEvent) {
        console.log('dragEnd', m, $event);
    }
}
