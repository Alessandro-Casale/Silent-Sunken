## [0.2.0] - 02-09-2026


New Features

- Register Resonant Stone block and Resonant Hammer item. ([#3](https://github.com/Alessandro-Casale/Silent-Sunken/pull/3))
- Add scan session model and server search/outline engine. ([#4](https://github.com/Alessandro-Casale/Silent-Sunken/pull/4))
- Sync scan sessions to clients over network. ([#5](https://github.com/Alessandro-Casale/Silent-Sunken/pull/5))
- Add client-side outline and camera shake managers. ([#6](https://github.com/Alessandro-Casale/Silent-Sunken/pull/6))
- Render resonance scan sphere and block outlines. ([#7](https://github.com/Alessandro-Casale/Silent-Sunken/pull/7))
- Add datagen and generated assets for Resonant Stone/Hammer. ([#8](https://github.com/Alessandro-Casale/Silent-Sunken/pull/8))
- Trigger resonance scan sessions while breaking discoverable blocks. ([#12](https://github.com/Alessandro-Casale/Silent-Sunken/pull/12))
- Add resonant crystal ore/crystal items and resonant barrel storage blocks with lock/unlock hint toasts. ([#13](https://github.com/Alessandro-Casale/Silent-Sunken/pull/13))
- Add block/item models, blockstates and item definitions for new blocks and items. ([#14](https://github.com/Alessandro-Casale/Silent-Sunken/pull/14))
- Add translations for resonant crystal, barrels, fragments and tablets. ([#15](https://github.com/Alessandro-Casale/Silent-Sunken/pull/15))
- Update block tags for resonant crystal ore. ([#16](https://github.com/Alessandro-Casale/Silent-Sunken/pull/16))
- Add block loot tables for resonant crystal ore and barrel blocks. ([#17](https://github.com/Alessandro-Casale/Silent-Sunken/pull/17))
- Add mossable data map system for applying and removing moss from blocks. ([#18](https://github.com/Alessandro-Casale/Silent-Sunken/pull/18))
- Add ruins structure worldgen and resonant crystal ore generation. ([#19](https://github.com/Alessandro-Casale/Silent-Sunken/pull/19))
- Add differentiated ruins chest loot tables themed around copper, stone and moss. ([#20](https://github.com/Alessandro-Casale/Silent-Sunken/pull/20))

Bug Fixes

- Replace fixed-cube sound interception scan with per-instance radius via active listener registry. ([#11](https://github.com/Alessandro-Casale/Silent-Sunken/pull/11))

API Changes

- Add nullability annotations. ([#1](https://github.com/Alessandro-Casale/Silent-Sunken/pull/1))
- Add time and vector utility helpers. ([#2](https://github.com/Alessandro-Casale/Silent-Sunken/pull/2))
- Add shared utility helpers for block entities, block states, containers, interaction checks and impact sounds. ([#9](https://github.com/Alessandro-Casale/Silent-Sunken/pull/9))
- Add SoundSensible contract and data-driven sound hint definitions. ([#10](https://github.com/Alessandro-Casale/Silent-Sunken/pull/10))
- Relocate CameraAngleEvents out of the hook.fx subpackage. ([#21](https://github.com/Alessandro-Casale/Silent-Sunken/pull/21))


