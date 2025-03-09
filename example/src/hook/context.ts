import { createContext, useContext } from 'react'
import {
  Config,
  defaultOptions,
} from 'react-native-multiple-image-picker-editor'

export const AppContext = createContext<{
  options: Config
  setOptions: (key: keyof Config, value: Config[keyof Config]) => void
}>({
  options: defaultOptions,
  setOptions: () => {},
})

export const useAppContext = () => useContext(AppContext)
