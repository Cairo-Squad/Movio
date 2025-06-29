# 🎬 Prepare Movies Task

This project sets up the foundation for our Movies app. Each squad is responsible for handling a few key tasks before feature development begins.

---

## ✅ What to Do

### 1. Modularization
Set up the project using the structure assigned to your squad:

- **By Feature**: Paris, Sanaa, Rome  
- **By Component**: Amsterdam, Baghdad, Berlin, Cairo  
- **By Layer (Presentation/Data/Domain)**: Moscow, London, Qudus, Washington DC  

### 2. Design System
Use the design system for your squad:

- **Aflami** → Paris, Amsterdam, Berlin  
- **Novix** → Sanaa, Baghdad, London  
- **CineVerse** → Rome, Moscow, Qudus  
- **Movio** → Cairo, Washington DC  

> Presentation modules should only depend on the `design_system` module — **do not add Material 3 directly**.

### 3. Firebase Integration
Integrate these Firebase tools:
- App Performance Monitoring  
- Analytics  
- Crashlytics  

### 4. CI/CD Setup

- ✅ CI must check that the app **builds** before merging into `develop`.
- ✅ Test coverage must be **over 80%** to allow merging.
- ✅ CD should build and upload a new APK/app bundle to **Firebase App Distribution** after merging.

---

## ⭐ Extra Task

Create a new module:  
**Image Viewer Library** using Coil.

- Should expose one `Image` composable.
- Use on-device machine learning to **blur images** that may be inappropriate in **Islamic culture**.

---

## 🧰 Required Libraries

Make sure to use:

- `ktor` → Networking  
- `koin` → Dependency Injection  
- `Navigation 2` → Navigation  
- `Coil` → Image Loading  

---

## 🧠 Notes

- Don’t rush — it’s okay if some members don’t have tasks this week.
- Focus only on the tasks listed above.
- Keep code clean and modular.

---

Let’s build something great — together! 🚀
