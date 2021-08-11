import React from 'react'
import { YMaps, Map, Placemark } from "react-yandex-maps";
import styled from "styled-components";

const LocationContainer = styled.div`
  justify-content: center;
`;

const Location = (props) => {

  const mapData = {
    center: [props.locationCoordinateX, props.locationCoordinateY],
    zoom: props.zoom,
  };

  const coordinates = [
    [props.locationCoordinateX, props.locationCoordinateY],
  ];

  return (
    <LocationContainer>
      <YMaps>
        <Map width={`@media (max-width: 300px)` ? "250px" : "300px"} defaultState={mapData}> 
          {coordinates.map(coordinate => <Placemark key={coordinate.indexOf} geometry={coordinate} />)}
        </Map>
      </YMaps>
    </LocationContainer>        
  )
}

export default Location
