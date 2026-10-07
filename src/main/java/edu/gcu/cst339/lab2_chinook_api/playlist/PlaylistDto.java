package edu.gcu.cst339.lab2_chinook_api.playlist;

public record PlaylistDto (
    Integer playlistId, //playlist's primary key
    String name //playlist's name
) {}
