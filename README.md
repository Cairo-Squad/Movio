# 🎬 Cairo Squad – Movies App Preparation

Welcome to our Movies app project!  
We are **Cairo Squad** (part of the Movio team), and this README outlines the setup and responsibilities we've taken on during the preparation phase.

---

## ✅ What We've Done

### 🧱 Modularization: By Component

We followed the **By Component** structure:  
Each part of the app (e.g., networking, UI components, image loader) is separated into reusable modules.

Examples:
- `core_networking`
- `core_ui`
- `core_image_viewer`
- `feature_home`

---

### 🎨 Design System: Movio

As part of the **Movio** team, we implemented the **Movio design system** in a separate `design_system` module.

✅ Presentation modules **only depend on `design_system`**.  
🚫 We did **not** add Material 3 directly to any screen.

---

### 🔥 Firebase Setup

We integrated the following Firebase tools into the app:

- App Performance Monitoring  
- Analytics  
- Crashlytics  

---

### ⚙️ CI/CD Setup

#### ✅ CI (Continuous Integration)

- Checks that the project **builds** before merging into `develop`.
- Ensures **test coverage is above 80%** — otherwise, merging is blocked.

#### 🚀 CD (Continuous Deployment)

- When a PR is approved and merged into `develop`, a new **APK/app bundle** is automatically built and uploaded to **Firebase App Distribution**.

---

## ⭐ Extra Work: Custom Image Viewer

We created a new module: `core_image_viewer`

- Built on top of **Coil**
- Exposes a single `Image` composable
- Uses on-device ML to **blur inappropriate content** (based on Islamic culture)

---

## 🧰 Libraries We Used

| Purpose         | Library   |
|----------------|-----------|
| Networking      | ktor      |
| Dependency Injection | koin  |
| Navigation      | Navigation 2 |
| Image Loading   | Coil      |

---

## 🧠 Notes

- We focused only on the required tasks.
- Some teammates didn’t have tasks this week — and that’s totally okay.
- Code is clean, modular, and ready for feature development.

---

### 🏁 We're ready for the next sprint!  
**– Cairo Squad 🚀**
