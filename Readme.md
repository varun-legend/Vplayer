# VPlayer

**VPlayer** — simple, lightweight video player for Android (legacy project).

A compact Android video player built with ExoPlayer (media3) and classic Android views. Features include file browsing via MediaStore & SAF, an ExoPlayer `PlayerView` with custom controls, gesture-based seek/volume/brightness, pinch-to-zoom, subtitle timing adjustments, and Picture-in-Picture support.

This project is released under the **MIT License**. See the `LICENSE` section below for the full text.

---

## Highlights / Features

- Uses **media3 / ExoPlayer** for playback.
- Custom `PlayerView` controller layout and custom controls.
- Gesture controls:
  - Horizontal swipe → seek
  - Vertical left/right → brightness / volume
  - Double-tap → speed toggle
  - Pinch → zoom & pan
  - Fling gestures used for subtitle timing adjustments
- Simple file listing using `MediaStore` (video) with thumbnail loading via Glide.
- Picture-in-Picture support on supported devices.

---

## Project structure & file overview

Below is a compact overview of the key files in the repository (based on the project snapshot you provided):

### Root / app
- `app/src/main/AndroidManifest.xml`  
  App manifest and permission declarations (legacy READ_EXTERNAL_STORAGE + Android 13+ granular media permissions, MANAGE_EXTERNAL_STORAGE opt-in, INTERNET, etc.).

### Resources
- `app/src/main/res/values/styles.xml` — theme & styling.
- `app/src/main/res/values/colors.xml` — color palette used by the app.
- `app/src/main/res/values/strings.xml` — user-facing strings.
- `app/src/main/res/layout/activity_main.xml` — main activity layout with the `RecyclerView` that lists media.
- `app/src/main/res/layout/item_media.xml` — single media item view (thumbnail, name, duration).
- `app/src/main/res/layout/activity_player.xml` — player activity layout containing `PlayerView`.
- `app/src/main/res/layout/custom_player_controls.xml` — custom controller layout used by `PlayerView`.
- `app/src/main/res/menu/player_menu.xml` — player options (speed, zoom reset, subtitle settings).
- `app/src/main/res/drawable/*` — shape drawables & vectors like launcher foreground/background, play button bg, placeholder.
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` — adaptive icon.

### Kotlin source (`app/src/main/java/com/varunlegend/vplayer`)
- `MainActivity.kt`  
  Requests permissions, queries `MediaStore.Video` and populates a `RecyclerView`. Launches `PlayerActivity` with selected URI.

- `PlayerActivity.kt`  
  Sets up `ExoPlayer` (`media3`) and `PlayerView`. Handles gestures (double-tap speed toggle, swipe-seek, volume/brightness), PiP entry on home, lifecycle handling, and uses helper classes for zoom & subtitle gestures.

- `MediaItemModel.kt`  
  Simple data class for media items (name, duration, uri).

- `adapters/MediaAdapter.kt`  
  RecyclerView adapter. Loads thumbnails via `MediaStore` + Glide, binds name/duration and click handler.

- `utils/MediaUtils.kt`  
  Helper utilities: building player, extracting duration using `MediaMetadataRetriever`, and showing playback speed dialog.

- `utils/ZoomPanListener.kt`  
  Pinch-to-zoom / pan logic; applies a matrix transform on the `TextureView` surface.

- `utils/SubtitleGestureHelper.kt`  
  Detects fling gestures to shift subtitle timing and provides dialogs to change subtitle delay/size.

---

## Build & run (legacy notes)

1. Open in Android Studio (matching an older Android Gradle plugin / Kotlin version the project used).
2. Ensure `minSdk`, `targetSdk` and Gradle plugin versions are set appropriately for media3 (ExoPlayer) and your environment.
3. Give the app appropriate runtime permissions:
   - On Android ≤ 12: `READ_EXTERNAL_STORAGE`
   - On Android 13+: `READ_MEDIA_VIDEO` and `READ_MEDIA_AUDIO`
   - (Optional) `MANAGE_EXTERNAL_STORAGE` if you rely on broad file-tree operations via SAF — beware scoped storage rules.
4. Run on a device/emulator with media files.

---

## Notes & caveats

- This is a **legacy** project snapshot. If upgrading to modern Android or Compose/Jetpack libraries, consider migrating UI and player lifecycle handling.
- If you plan to distribute on Play Store, re-check permission usage (esp. `MANAGE_EXTERNAL_STORAGE`) and targetSdk requirements.
- Thumbnail API usage and direct access to internal settings may need adjustments for newer Android versions and permissions.

---

## License

This project is licensed under the **MIT License**
see the Licence file for details

---

*End of README content. ✨*
