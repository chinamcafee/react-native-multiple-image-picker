import React from 'react'
import { View as RNView, ViewProps as RNViewProps } from 'react-native'
import useTheme from '../hook/useTheme'

export interface ViewProps extends RNViewProps {
  level?: 0 | 1 | 2 | 3
  flex?: number
}

export function View({
  children,
  style: containerStyle,
  level = 0,
  flex,
}: ViewProps) {
  const theme = useTheme()
  const backgroundColor = !level
    ? theme.background
    : theme[`background_${level}` as keyof typeof theme]

  // Create style object conditionally to avoid invalid flex values
  const baseStyle: { backgroundColor: string; flex?: number } = { backgroundColor }
  if (typeof flex === 'number' && !isNaN(flex) && flex >= 0) {
    baseStyle.flex = flex
  }

  return (
    <RNView style={[baseStyle, containerStyle]}>
      {children}
    </RNView>
  )
}
