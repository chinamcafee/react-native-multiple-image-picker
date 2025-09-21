import { useColorScheme } from 'react-native'
import * as color from '../theme/color'

export default function useTheme() {
  const colorScheme = useColorScheme()
  
  // Provide fallback to 'light' if colorScheme is null or undefined
  const safeColorScheme = (colorScheme || 'light') as keyof typeof color

  return color[safeColorScheme]
}
