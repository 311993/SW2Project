# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## [2024.10.14]

### Added
- Interfaces for InventoryKernel, Inventory, and Item.
- `isAllowed(Item)` kernel method to allow client to check an Item against restrictions and to facilitate `addItem` changes below.
- `isAt(int slot, String name)` secondary method header to conveniently check whether an item is at a certain slot.
- Full method contracts for InventoryKernel, Inventory, and Item methods.

### Updated
- Changed kernel method `restrictIntake()` to  `restrict(String)` for brevity and implementation flexibility.
- changed `addItem` kernel method from boolean to void return type and added conditions which would previously cause a return of false to the requires clause.
- Changed `copyItem` and `transferItem` secondary method return types to void and added requires clause conditions in line with the changes to `addItem` above.
-Changed `useItem` secondary method to return the name of the item used.

## [2024.10.02]

### Added
- Added demonstrations of client usage of Inventory proof of concept
- Completed 02-component-proof-of-concept

### Updated
- Fixed bugs in `nextPlacement` and `nextIndexOf` searching
- Added secondary method `copyItem` to Inventory proof of concept

## [2024.09.30]

### Added
- Implemented a proof of concept for an Inventory component
- Implemented a proof of concept Item internal class for Inventory component
- Added a demo of proof of concept's client usage

### Updated
- Changed `hasItem` kernel method to `nextIndexOf` to more accurately reflect its usage
- Changed design to include `transferItem` secondary method
- Changed `transferItems` secondary method to an overload of `swapItems` to more accurately reflect its usage and prevent name confusion with `transferItem`
- Changed design of secondary method `nextEmptySlot` to `nextPlacement` for more versatile usage

## [2024.09.16]

### Added

- Designed a Matrix component
- Designed a IndexedColorImage component
- Designed a Inventory component
- Completed 01-component-brainstorming