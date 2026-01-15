# 🎬 VPlayer

**VPlayer** is a simple, lightweight Android video player built using **ExoPlayer (media3)** and classic Android Views.

This was my **first Android app project**. It’s not perfect, it’s not bleeding-edge — but it *works*, and it taught me a lot.

Originally, I planned to keep this project proprietary. But since it’s no longer actively maintained, I decided to **open-source it instead** so early Android developers can learn from it, explore real playback logic, and build on top of it freely.

One fun constraint:  
👉 **This project was built entirely using GitHub Actions** — no desktop IDEs, no Android Studio. Everything was edited, built, and tested through CI.

If this helps even one beginner understand media playback better, it’s worth it ❤️

---

## ✨ Features

- 🎥 Powered by **media3 / ExoPlayer**
- 🎛️ Custom `PlayerView` controller layout
- 📂 Video browsing via **MediaStore** and **SAF**
- 🤏 Rich gesture controls:
  - Horizontal swipe → Seek
  - Vertical swipe (left/right) → Brightness / Volume
  - Double-tap → Playback speed toggle
  - Pinch → Zoom & pan
  - Fling → Subtitle timing adjustment
- 🖼️ Video thumbnails via **Glide**
- 🪟 **Picture-in-Picture (PiP)** support
- 🧩 Clean, minimal, and easy-to-follow structure

---

## 🧠 Project Overview

### App structure

```
app/
 └─ src/main/
    ├─ AndroidManifest.xml
    ├─ java/com/varunlegend/vplayer
    │  ├─ MainActivity.kt
    │  ├─ PlayerActivity.kt
    │  ├─ MediaItemModel.kt
    │  ├─ adapters/
    │  │  └─ MediaAdapter.kt
    │  └─ utils/
    │     ├─ MediaUtils.kt
    │     ├─ ZoomPanListener.kt
    │     └─ SubtitleGestureHelper.kt
    └─ res/
       ├─ layout/
       ├─ values/
       ├─ drawable/
       └─ mipmap/
```

---

## 🧩 Key Files Explained

### `MainActivity.kt`
- Handles runtime permissions
- Queries `MediaStore.Video`
- Displays videos in a `RecyclerView`
- Launches `PlayerActivity` with the selected video URI

### `PlayerActivity.kt`
- Sets up **ExoPlayer (media3)**
- Hosts the `PlayerView`
- Handles:
  - Gestures (seek, brightness, volume, speed)
  - Picture-in-Picture mode
  - Player lifecycle events
  - Zoom & subtitle helpers

### `MediaAdapter.kt`
- RecyclerView adapter for video list
- Loads thumbnails using Glide
- Displays title and duration

### `ZoomPanListener.kt`
- Implements pinch-to-zoom and panning
- Applies matrix transforms to the video surface

### `SubtitleGestureHelper.kt`
- Uses fling gestures to adjust subtitle timing
- Provides subtitle delay & size controls

---

## 🛠️ Build & Run (Legacy Notes)

1. Open the project in **Android Studio**
2. Use compatible versions of:
   - Android Gradle Plugin
   - Kotlin
   - media3 / ExoPlayer
3. Grant runtime permissions:
   - **Android ≤ 12** → `READ_EXTERNAL_STORAGE`
   - **Android 13+** → `READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO`
   - Optional: `MANAGE_EXTERNAL_STORAGE` (be careful with Play Store rules)
4. Run on a real device or emulator with video files

---

## ⚠️ Notes & Caveats

- This is a **legacy project**
- Not optimized for modern Jetpack Compose or latest Android APIs
- If publishing to Play Store:
  - Re-check storage permissions
  - Update `targetSdk`
  - Follow scoped storage guidelines
- Great as:
  - A learning reference
  - A base for experiments
  - A starter for custom players

---

## 📜 License

This project is released under the **MIT License**.

You are free to:
- Use it
- Modify it
- Learn from it
- Ship it

See the `LICENSE` file for full details.

---

## ❤️ Final Note

This project isn’t meant to compete with VLC or MX Player.  
It’s meant to show **how a real Android video player works under the hood**.

If you’re a beginner — clone it, break it, improve it.  
That’s exactly why it’s open source 😊
