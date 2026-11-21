# Project Plan

Daily Scribe: An offline-first Android application designed to help Christians build and maintain consistent daily devotion habits. Features include Devotional Activity Tracker (streak log, contribution heatmap), Dual-Mode Journaling & OCR (digital entry with draft auto-save, physical paper OCR via CameraX & Google ML Kit v2 with quick-edit correction UI, social sharing with AI summarization/rephrasing), Modular Bible Reader (decoupled translation engine with BSB/WEB offline support), Smart Notification Engine (configurable daily devotion targets default 2/day, pre-devotion reminder, randomized follow-up logic capped at 2 per missed devotion, suppressed from 12 AM onwards, surviving reboots). Built with Kotlin, Jetpack Compose, MVVM + Clean Architecture, Room/SQLDelight encrypted with SQLCipher, Supabase backend + BiometricPrompt, CameraX + ML Kit v2, WorkManager + AlarmManager. Planned roadmap for future V2 includes cross-platform sync, AI reflection chatbot, app blocker with NLP excuse evaluator.

## Project Brief

# Project Brief: Daily Scribe

Daily Scribe is an offline-first Android application designed to help Christians build and maintain consistent daily devotion habits through visual streak tracking, dual-mode journaling with physical paper OCR scanning, offline Bible reading, and persistent smart notifications.

## Features

1. **Devotional Activity Tracker**: Visual streak log and contribution heatmap to track daily devotion progress and foster consistent spiritual habits.
2. **Dual-Mode Journaling & OCR**: Digital devotion entry with draft auto-save, integrated with CameraX and Google ML Kit OCR to digitize handwritten physical journal entries with quick-edit correction UI.
3. **Modular Offline Bible Reader**: Offline scripture reader powered by a decoupled translation engine supporting BSB (Berean Standard Bible) and WEB (World English Bible) translations.
4. **Smart Notification Engine**: Configurable daily devotion targets with reboot-resilient reminders, pre-devotion alerts, and controlled follow-up notification logic.

## High-Level Tech Stack

* **Language & Async**: Kotlin, Coroutines & Flow
* **UI Framework & Layout**: Jetpack Compose, **Compose Material Adaptive** library for all screen layouts
* **Navigation**: **Jetpack Navigation 3** (state-driven navigation)
* **Architecture**: MVVM + Clean Architecture
* **Persistence & Security**: Room (encrypted with SQLCipher for secure offline storage)
* **Camera & Machine Learning**: CameraX & Google ML Kit v2 (Text Recognition)
* **Background Scheduling**: WorkManager & AlarmManager

## Implementation Steps

### Task_1_CoreArchitectureAndDataPersistence: Set up project data models, Room database with SQLCipher encryption, Repository pattern, and Jetpack Navigation 3 baseline structure for Daily Scribe.
- **Status:** COMPLETED
- **Updates:** Task 1 completed successfully. Database setup with Room & SQLCipher, Clean Architecture layers, Domain entities/interfaces/use-cases, Data repositories/mappers/DAOs, ViewModels, Compose adaptive navigation layout, and Unit tests implemented and verified via gradlew assembleDebug and testDebugUnitTest.
- **Acceptance Criteria:**
  - Room database initialized with encrypted SQLCipher support
  - Entities for Journal Entries, Devotional Logs, Bible Scripture, and Reminders created
  - Jetpack Navigation 3 state-driven navigation baseline configured
  - build pass

### Task_2_DevotionalTrackerAndJournalingOCR: Implement Devotional Activity Tracker (streaks and contribution heatmap) and Dual-Mode Journaling with CameraX integration and Google ML Kit OCR text recognition.
- **Status:** COMPLETED
- **Updates:** Task 2 completed: Devotional Activity Tracker with heatmap contribution grid and streak calculations implemented. Dual-Mode Journaling with digital draft auto-save, CameraX live viewfinder, Google ML Kit v2 OCR text recognition, quick-edit correction dialog, and social sharing helper successfully implemented. Code structure and unit tests verified.
- **Acceptance Criteria:**
  - Devotional activity tracker renders streaks and heatmap correctly
  - Journal entry interface supports auto-saving drafts
  - CameraX live preview and ML Kit OCR extracts text from handwritten/printed journals
  - build pass

### Task_3_OfflineBibleReader: Implement Modular Offline Bible Reader powered by decoupled translation engine supporting BSB and WEB translations with search and navigation.
- **Status:** COMPLETED
- **Updates:** Task 3 completed: Modular Offline Bible Reader implemented with BSB and WEB offline support, Room database pre-seeding, Book/Chapter navigation, search filtering, translation switcher, and Compose Material Adaptive layout. Verified with successful build.
- **Acceptance Criteria:**
  - Offline Bible Reader supports switching between BSB and WEB translations
  - Scripture navigation by book, chapter, and verse functions offline
  - Search and verse selection UI implemented using Compose Material Adaptive
  - build pass

### Task_4_SmartNotificationEngine: Implement Smart Notification Engine using WorkManager and AlarmManager for reboot-resilient daily devotion reminders and pre-devotion alerts.
- **Status:** COMPLETED
- **Updates:** Task 4 completed: Smart Notification Engine successfully implemented with AlarmManager primary reminders, BootReceiver for reboot resilience, WorkManager background follow-up worker (capped at max 2 follow-ups, suppressed from 12 AM onwards), settings UI wiring, and successful build/test verification.
- **Acceptance Criteria:**
  - Daily devotion targets and notification preferences configurable in UI
  - AlarmManager schedules reboot-resilient daily reminders
  - WorkManager handles background follow-up notification logic
  - build pass

### Task_5_RunAndVerify: Integrate all screens and background services into a unified MVP flow, run end-to-end verification, and instruct critic_agent to verify application stability, performance, and UI alignment.
- **Status:** COMPLETED
- **Updates:** Task 5 completed successfully: End-to-end verification performed. `./gradlew assembleDebug` built cleanly, unit tests passed with 0 failures, and all features (Activity Tracker, Dual-Mode Journaling & OCR, Offline Bible Reader, Smart Notification Engine) verified across adaptive layouts.
- **Acceptance Criteria:**
  - All navigation flows work seamlessly end-to-end
  - make sure all existing tests pass
  - build pass
  - app does not crash
- **Duration:** N/A

