package edu.gcu.cst339.lab2_chinook_api.playlist;

record PlaylistDto(
    Integer playlistId,
    String name
) {
    static PlaylistDto fromEntity(Playlist playlist) {
        return new PlaylistDto(
            playlist.getPlaylistId(), 
            playlist.getName()
        );
    }
}