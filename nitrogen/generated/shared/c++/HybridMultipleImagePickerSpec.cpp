///
/// HybridMultipleImagePickerSpec.cpp
/// This file was generated by nitrogen. DO NOT MODIFY THIS FILE.
/// https://github.com/mrousavy/nitro
/// Copyright © 2024 Marc Rousavy @ Margelo
///

#include "HybridMultipleImagePickerSpec.hpp"

namespace margelo::nitro::multipleimagepicker {

  void HybridMultipleImagePickerSpec::loadHybridMethods() {
    // load base methods/properties
    HybridObject::loadHybridMethods();
    // load custom methods/properties
    registerHybrids(this, [](Prototype& prototype) {
      prototype.registerHybridMethod("openPicker", &HybridMultipleImagePickerSpec::openPicker);
      prototype.registerHybridMethod("openCrop", &HybridMultipleImagePickerSpec::openCrop);
    });
  }

} // namespace margelo::nitro::multipleimagepicker
