export enum MultipleImagePickerError {
  CANCELLED = 0,
}

export enum CameraError {
  INVALID_OUTPUT_FILE = 1,
}

export const CropReject = {
  0: {
    code: 0,
    message: 'Invalid Image',
  },
  1: {
    code: 1,
    message: 'User Cancelled',
  },
}
